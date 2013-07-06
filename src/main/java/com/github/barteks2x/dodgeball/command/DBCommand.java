package com.github.barteks2x.dodgeball.command;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DBCommand {
}
