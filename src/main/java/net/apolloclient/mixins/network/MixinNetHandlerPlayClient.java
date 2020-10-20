/*⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼
  Copyright (C) 2020-2021 developed by Icovid and Apollo Development Team
  
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published
  by the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.
  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see https://www.gnu.org/licenses/.
  
  Contact: Icovid#3888 @ https://discord.com
 ⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼*/

package net.apolloclient.mixins.network;

import net.apolloclient.Apollo;
import net.apolloclient.event.impl.client.ActionBarEvent;
import net.apolloclient.event.impl.client.ChatReceivedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S02PacketChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * {@link Mixin} target injections for events derived from {@link NetHandlerPlayClient}.class
 *
 * @author Nora Cos | Nora#0001
 * @see NetHandlerPlayClient target
 * @since 1.2.0-BETA
 */
@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    /**
     * Post {@link ActionBarEvent} or {@link ChatReceivedEvent} at chat packet.
     *
     * @param callbackInfo unused variable to cancel remaining method
     * @param packetIn     chat packet received by server
     *
     * @see NetHandlerPlayClient#handleChat(S02PacketChat) target
     */
    @Inject(method = "handleChat", at = @At(value = "HEAD"), cancellable = false)
    private void onChatPacket(S02PacketChat packetIn, CallbackInfo callbackInfo) {
        if (packetIn.getType() == 2) Apollo.EVENT_BUS.post(new ActionBarEvent(packetIn.getChatComponent()));
        else Apollo.EVENT_BUS.post(new ChatReceivedEvent(packetIn.getChatComponent()));
    }
}
