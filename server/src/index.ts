import dotenv from "dotenv"
dotenv.config()
import "reflect-metadata"
import express from "express"
import { createConnection } from "typeorm"
import BugRoutes from "./routes/BugRoutes"

const PORT = process.env.PORT

const main = async () => {

    const app = express()
    app.use(express.json())
    await createConnection()

    app.get("/", (_, res) => {
        res.send("Hello Server")
    })

    app.use(BugRoutes)

    app.listen(PORT, () => {
        console.log(`http://localhost:${PORT}`)
    })
}

main().catch(e => console.log('Error in main: ', e))