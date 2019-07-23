package com.bird.netty.server.help;

import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.IMessage;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Objects;

/**
 * @Author: bird
 * @Date: 2019/6/19 10:17
 */
public class MachineStateSendClientChooser {

    private static Multimap<String, Cli> MACHINE_STATE_SENDER = LinkedHashMultimap.create();

    private static class Cli {
        private boolean leading;
        private final Channel channel;

        public Cli(Channel channel, boolean leading) {
            this.channel = channel;
            this.leading = leading;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cli)) return false;
            Cli cli = (Cli) o;
            return Objects.equals(channel, cli.channel);
        }

        @Override
        public int hashCode() {
            return channel.hashCode();
        }

        public void markLeading() {
            this.leading = true;
        }
    }


    public static void registerSendMachineInfo(Channel channel) {
        String machineId = extractMachineId(channel);

        synchronized (MACHINE_STATE_SENDER) {
            Cli cli = new Cli(channel, false);

            if (MACHINE_STATE_SENDER.containsKey(machineId)) {
                MACHINE_STATE_SENDER.put(machineId, cli);
            } else {
                MACHINE_STATE_SENDER.put(machineId, cli);
                notifyAsMachineSender(cli);
            }
        }
    }

    public static String extractMachineId(Channel channel) {
        SocketAddress socketAddress = channel.remoteAddress();
        String machineId;
        if (socketAddress instanceof InetSocketAddress) {
            machineId = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
        } else {
            machineId = socketAddress.toString();
            int i = machineId.indexOf(":");
            if (i > 0) {
                machineId = machineId.substring(0, i);
            }
        }
        return machineId;
    }

    public static void remove(Channel channel) {
        String machineId = extractMachineId(channel);
        synchronized (MACHINE_STATE_SENDER) {
            if (MACHINE_STATE_SENDER.remove(machineId, new Cli(channel, true))) {
                Collection<Cli> clis = MACHINE_STATE_SENDER.get(machineId);
                for (Cli c : clis) {
                    if (c.leading) {
                        return;
                    }
                }

                if (clis != null && clis.size() > 0) {
                    notifyAsMachineSender(clis.iterator().next());
                }
            }

        }
    }


    private static void notifyAsMachineSender(Cli cli) {
        cli.leading = true;
        IMessage iMessage = IMessage.of(HandleType.AS_MACHINE_SENDER, "you election for message sender!");
        cli.channel.writeAndFlush(iMessage);
    }

}
