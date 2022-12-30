export function nameFromValue<T extends Record<string, string>>(stringEnum: T, value: string) {
  return Object.keys(stringEnum)[Object.values(stringEnum).indexOf(value)]
}
