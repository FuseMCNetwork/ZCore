package net.fusemc.zcore.shopAPI.packets;

import com.xxmicloxx.znetworklib.codec.CodecResult;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.codec.PacketReader;
import com.xxmicloxx.znetworklib.codec.PacketWriter;

/**
 * Copyright by michidk
 * Created: 22.08.2014.
 */
public class UpdatePackagesEvent implements NetworkEvent {

    public static final String eventName = "buycraftclient-updatpackages";

    private String uuid;
    private String packageName;
    private String action;

    public UpdatePackagesEvent() {

    }

    public UpdatePackagesEvent(String uuid, String packageName, String action) {
        this.uuid = uuid;
        this.packageName = packageName;
        this.action = action;
    }

    @Override
    public CodecResult write(PacketWriter writer) {
        writer.writeString(uuid);
        writer.writeString(packageName);
        writer.writeString(action);
        return CodecResult.OK;
    }

    @Override
    public CodecResult read(PacketReader reader) {
        uuid = reader.readString();
        packageName = reader.readString();
        action = reader.readString();
        return CodecResult.OK;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAction() {
        return action;
    }
}
