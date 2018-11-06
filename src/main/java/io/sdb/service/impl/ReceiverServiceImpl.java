package io.sdb.service.impl;

import io.sdb.dao.ReceiverDao;
import io.sdb.model.Receiver;
import io.sdb.service.ReceiverService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReceiverServiceImpl extends BaseServiceImpl<ReceiverDao, Receiver> implements ReceiverService {

    @Override
    public Receiver findDefault(String userId) {
        Receiver qr = new Receiver();
        qr.setMemberId(userId);
        qr.setIsDefault(true);
        Receiver receiver = this.findFirstByModel(qr);
        return receiver;
    }
}
