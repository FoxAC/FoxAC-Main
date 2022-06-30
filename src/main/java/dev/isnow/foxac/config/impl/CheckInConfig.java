package dev.isnow.foxac.config.impl;

import dev.isnow.foxac.check.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 5170
 * made on dev.isnow.foxac.config
 */

@AllArgsConstructor@Getter
public class CheckInConfig {
    Category category;
    String description;
    int maxvl;
    Boolean enabled;
}
