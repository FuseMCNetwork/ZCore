package net.fusemc.zcore.featureSystem.features.corpseFeature.connection;


import net.minecraft.server.v1_7_R4.*;

public class NullConnection extends PlayerConnection
{
    public NullConnection(MinecraftServer minecraftserver, NetworkManager inetworkmanager, EntityPlayer entityplayer)
    {
        super(minecraftserver, inetworkmanager, entityplayer);
    }

    @Override
    public void a(PacketPlayInWindowClick packet)
    {
    }

    @Override
    public void a(PacketPlayInTransaction packet)
    {
    }

    @Override
    public void a(PacketPlayInFlying packet)
    {
    }

    @Override
    public void a(PacketPlayInUpdateSign packet)
    {
    }

    @Override
    public void a(PacketPlayInBlockDig packet)
    {
    }

    @Override
    public void a(PacketPlayInBlockPlace packet)
    {
    }

    @Override
    public void disconnect(String s)
    {
    }

    @Override
    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot)
    {
    }

    @Override
    public void a(PacketPlayInChat packetplayinchat)
    {
    }

    @Override
    public void sendPacket(Packet packet)
    {
    }
}