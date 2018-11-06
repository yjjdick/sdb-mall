package io.sdb.dao;

import io.sdb.model.Receiver;
import org.springframework.stereotype.Component;

@Component
public class ReceiverDao extends BaseDao<Receiver> {
    public ReceiverDao() {
        super(Receiver.class);
    }
}
