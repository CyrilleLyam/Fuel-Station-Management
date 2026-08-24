function isPlainObject(value: unknown): value is Record<string, unknown> {
  return (
    typeof value === "object" &&
    value !== null &&
    !Array.isArray(value) &&
    !(value instanceof Date) &&
    !(value instanceof File) &&
    !(value instanceof Blob) &&
    !(value instanceof FormData)
  );
}

function camelToSnake(key: string): string {
  return key.replace(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`);
}

function snakeToCamel(key: string): string {
  return key.replace(/_([a-z0-9])/g, (_, char: string) => char.toUpperCase());
}

export function toSnakeCase<T>(input: T): T {
  if (Array.isArray(input)) {
    return input.map((item) => toSnakeCase(item)) as T;
  }
  if (isPlainObject(input)) {
    return Object.fromEntries(
      Object.entries(input).map(([key, value]) => [
        camelToSnake(key),
        toSnakeCase(value),
      ]),
    ) as T;
  }
  return input;
}

export function toCamelCase<T>(input: T): T {
  if (Array.isArray(input)) {
    return input.map((item) => toCamelCase(item)) as T;
  }
  if (isPlainObject(input)) {
    return Object.fromEntries(
      Object.entries(input).map(([key, value]) => [
        snakeToCamel(key),
        toCamelCase(value),
      ]),
    ) as T;
  }
  return input;
}
