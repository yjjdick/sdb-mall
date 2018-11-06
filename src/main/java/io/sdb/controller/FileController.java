package io.sdb.controller;

import io.sdb.common.exception.RRException;
import io.sdb.common.utils.R;
import io.sdb.enums.ResultEnum;
import io.sdb.model.SysOss;
import io.sdb.common.annotation.Login;
import io.sdb.oss.cloud.OSSFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    @Login
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }

        Long fileSize = file.getSize();
        if (fileSize > 2048 * 1000) {
            return R.error(ResultEnum.VOLUNTEER_UPLOAD_FILE_BIG);
        }

        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

        //保存文件信息
        SysOss sysOss = new SysOss();
        sysOss.setUrl(url);
        sysOss.setCreateDate(new Date());
        sysOss.save();

        return R.ok().put("url", url);
    }

}
