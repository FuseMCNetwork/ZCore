package net.fusemc.zcore.shopAPI.packets;

import com.xxmicloxx.znetworklib.codec.CodecResult;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.codec.PacketReader;
import com.xxmicloxx.znetworklib.codec.PacketWriter;

/**
 * Copyright by michidk
 * Created: 22.08.2014.
 */
public class UpdateCoinsEvent implements NetworkEvent {

    public static final String eventName = "buycraftclient-updatecoins";

    private String uuid;
    private int coins;

    public UpdateCoinsEvent() {

    }

    public UpdateCoinsEvent(String uuid, int coins) {
        this.uuid = uuid;
        this.coins = coins;
    }

    @Override
    public CodecResult write(PacketWriter writer) {
        writer.writeString(uuid);
        writer.writeInt(coins);
        return CodecResult.OK;
    }

    @Override
    public CodecResult read(PacketReader reader) {
        uuid = reader.readString();
        coins = reader.readInt();
        return CodecResult.OK;
    }

    public String getUuid() {
        return uuid;
    }

    public int getCoins() {
        return coins;
    }
}
