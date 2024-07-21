export const STOPPING_TICK_INTERVAL = 3 // when stopping, how long each slowed-down tick is
export const STOPPING_LENGTH = 5 // how many slowed-down ticks to show
export const TICK_INTERVAL = 250 // 1/4th of a second
export const TOTAL_TICKS = 600
export const CHOOSING_TICKS = 240
export const NUM_PLATES = 50
export const PORT = process.env.LOTTERY_PORT || 4402
const INTERNAL_SERVER_HOSTNAME = process.env.INTERNAL_SERVER || 'http://localhost'
const INTERNAL_PORT = process.env.INTERNAL_PORT || 4401
export const INTERNAL_SERVER = `${INTERNAL_SERVER_HOSTNAME}:${INTERNAL_PORT}`
export const SECRET = process.env.SECRET || "deadbeef"
export const VALID_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
export const VALID_NUMBERS = "0123456789"