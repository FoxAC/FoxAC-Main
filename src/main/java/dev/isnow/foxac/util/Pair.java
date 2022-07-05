package dev.isnow.foxac.util;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 5170
 * made on dev.isnow.foxac.util
 */

// Copied directly from javaFX

public class Pair<K,V> implements Serializable {
    private K key;

    public K getKey() { return key; }

    private V value;


    public V getValue() { return value; }

    public Pair(@NamedArgPair("key") K key, @NamedArgPair("value") V value) {
        this.key = key;
        this.value = value;
    }
}
@Retention(RUNTIME)
@Target(PARAMETER)
@interface NamedArgPair {
    public String value();

    public String defaultValue() default "";
}