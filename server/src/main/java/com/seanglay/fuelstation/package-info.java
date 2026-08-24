/**
 * Fuel Station Management application root.
 * <p>
 * Each direct sub-package of this package is a Spring Modulith module representing one
 * bounded context (e.g. {@code station}, {@code inventory}). Within a module, follow the
 * Clean Architecture dependency rule &mdash; source code dependencies point inward only:
 *
 * <pre>
 * &lt;module&gt;
 *   domain/          entities, value objects, domain events, repository ports (interfaces only)
 *   application/     use cases orchestrating domain objects; depends on domain only
 *   infrastructure/  JPA repository implementations, external adapters; depends on domain + application
 *   interfaces/      REST controllers, request/response records; depends on application only
 * </pre>
 * <p>
 * {@code domain} and {@code application} must not depend on {@code infrastructure} or
 * {@code interfaces}. Types meant to be used by other modules belong in the module's root
 * package (Spring Modulith's default named interface) or an explicit
 * {@code @NamedInterface}; everything else is module-private.
 * <p>
 * Cross-cutting building blocks shared by every module live in
 * {@link com.seanglay.fuelstation.shared}.
 */
package com.seanglay.fuelstation;
