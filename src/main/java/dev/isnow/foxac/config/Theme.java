package dev.isnow.foxac.config;

import lombok.AllArgsConstructor;

/**
 * @author 5170
 * made on dev.isnow.foxac.config
 */

// ik this is a bad way to store those but im too lazy to think
@AllArgsConstructor
public class Theme {
    String alerts, colorLow, colorMedium, experimental, prefix, alertsOn, alertsOff, vpnKick, banning, joinMessage, clientKick, broadcastMessage;
}
