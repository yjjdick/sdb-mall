package io.sdb.controller;

import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.enums.GeneralEnum;
import io.sdb.enums.GrouponStatusEnum;
import io.sdb.model.Groupon;
import io.sdb.model.GrouponTeam;
import io.sdb.model.User;
import io.sdb.service.GrouponService;
import io.sdb.service.GrouponTeamService;
import io.sdb.service.UserService;
import io.sdb.vo.GrouponDetailVO;
import io.sdb.vo.GrouponTeamVO;
import io.sdb.vo.GrouponVO;
import io.swagger.models.auth.In;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wechat/groupon")
public class GrouponController {

	@Autowired
	private GrouponService grouponService;

	@Autowired
	private GrouponTeamService grouponTeamService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/grouponTeam/{grouponId}")
    public R grouponTeam(@PathVariable String grouponId){
        Filter filter = new Filter();
        filter.setProperty("groupon_id");
        filter.setOperator(Filter.Operator.eq);
        filter.setValue(grouponId);

        List<GrouponTeam> grouponTeamList = grouponTeamService.findByFilter(filter);

        List<GrouponTeamVO> grouponTeamVOList = grouponTeamList.stream().map(item -> {
            GrouponTeamVO grouponTeamVO = new GrouponTeamVO();
            BeanUtils.copyProperties(item, grouponTeamVO);
            User user = userService.findById(item.getUserId());
            grouponTeamVO.setHeadUrl(user.getAvatar());
            return grouponTeamVO;
        }).collect(Collectors.toList());

        Groupon groupon = grouponService.findById(grouponId);
        Integer surplus = groupon.getCount() - grouponTeamList.size();
        if (surplus < 0) {
            surplus = 0;
        }

        return R.ok().put("grouponTeamList", grouponTeamVOList).put("surplus", surplus).put("grouponExpireTime", groupon.getExpireDate().getTime());
    }

	@ResponseBody
	@GetMapping("/grouponTop/{goodsId}")
	public R grouponTop(@PathVariable String goodsId){
        List<Filter> filterList = new ArrayList<>();
        Filter filter = new Filter();
		filter.setProperty("goods_id");
		filter.setOperator(Filter.Operator.eq);
		filter.setValue(goodsId);
        filterList.add(filter);

        filter = new Filter();
		filter.setProperty("expire_date");
		filter.setOperator(Filter.Operator.gt);
		filter.setValue(new Date());
        filterList.add(filter);

        filter = new Filter();
        filter.setProperty("status");
        filter.setOperator(Filter.Operator.eq);
        filter.setValue(GrouponStatusEnum.PENDING.getCode());
        filterList.add(filter);

		Order order = new Order();
		order.setDirection(Order.Direction.asc);
		order.setProperty("create_date");
		Page<Groupon> page = grouponService.paginate(1, 10, filterList, order);

        List<Groupon> grouponList = page.getList();

        List<GrouponDetailVO> grouponDetailVOList = grouponList.stream().map(item -> {
            GrouponDetailVO grouponDetailVO = new GrouponDetailVO();
            grouponDetailVO.setGrouponId(item.getId());
            grouponDetailVO.setTotalCount(item.getCount());

            List<Filter> gtFilterList = new ArrayList<>();

            Filter gtFilter = new Filter();
            gtFilter.setProperty("groupon_id");
            gtFilter.setOperator(Filter.Operator.eq);
            gtFilter.setValue(item.getId());
            gtFilterList.add(gtFilter);

            List<GrouponTeam> grouponTeamList = grouponTeamService.findByFilters(gtFilterList);
            GrouponTeam captain = new GrouponTeam();
            for (GrouponTeam grouponTeam : grouponTeamList) {
                if (grouponTeam.getCaptain() == GeneralEnum.TRUE.getCode()) {
                    captain = grouponTeam;
                    break;
                }
            }

            User user = userService.findById(captain.getUserId());
            grouponDetailVO.setHeadUrl(user.getAvatar());
            grouponDetailVO.setName(user.getNickname());
            grouponDetailVO.setJoinedCount(grouponTeamList.size());
            return grouponDetailVO;
        }).collect(Collectors.toList());

		return R.ok().put("grouponDetailList", grouponDetailVOList);
	}

}