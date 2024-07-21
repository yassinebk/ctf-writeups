const validatorFactory = require('@fastify/fast-json-stringify-compiler').SerializerSelector()()
const customValidators = Object.create(null, {}) // no more p.p.
const { v4: uuid } = require('uuid')
const FLAG = 'the old one';
const defaultSchema = {
    type: 'object',
    properties: {
        pong: {
            type: 'string',
        },
    },
}



function testValidator(newSchema) {

    // The proto pollution is happening here which causes an error
    const schema = Object.assign({}, defaultSchema, newSchema)
    console.log("[+] Final schema", schema)
    const validator = validatorFactory({ schema })

    return validator({ [FLAG]: 'congratulations' })
}

const newSchema = {

    type: 'object',
    properties: {
        'FLAG': {
            type: 'string',
            default: 'meow'
        },
        'the old one': {
            type: 'string'
        },
        'x': {
            type: 'object',
            properties: {
                admin: {
                    type: 'object',
                    default: '__proto__'
                }
            }
        }
    },

}
console.log(testValidator(newSchema, { mode: 'debug' }))
