export function pushToValue<K, V>(map: Map<K, V[]>, key: K, value: V) {
  const array = map.get(key)
  if (typeof array !== 'undefined') {
    array.push(value)
  } else {
    map.set(key, [value])
  }
}
