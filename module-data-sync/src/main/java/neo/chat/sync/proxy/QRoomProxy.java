package neo.chat.sync.proxy;

import neo.chat.persistence.query.document.QRoom;

import java.util.UUID;

public class QRoomProxy extends QRoom {

    public QRoomProxy(UUID id) {
        this.id = id;
    }

}
