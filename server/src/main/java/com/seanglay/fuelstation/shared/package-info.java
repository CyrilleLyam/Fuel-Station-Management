/**
 * Shared kernel: tactical DDD building blocks used across bounded-context modules.
 * <p>
 * This is an open Spring Modulith module &mdash; every other module may depend on it
 * freely. It must never depend on any other module.
 */
@org.springframework.modulith.ApplicationModule(type = org.springframework.modulith.ApplicationModule.Type.OPEN)
package com.seanglay.fuelstation.shared;
