export function rounded(value: number, decimals: number): number {
  const scale = Math.pow(10, decimals)
  return Math.round((value + Number.EPSILON) * scale) / scale
}
