import dotenv from "dotenv"
dotenv.config()
import "reflect-metadata"
import express from "express"
import { createConnection } from "typeorm"
import BugRoutes from "./routes/BugRoutes"
import { authentificateToken } from "./middleware/AuthMiddleware"
import AuthRoutes from "./routes/AuthRoutes"

const PORT = process.env.PORT

const main = async () => {

    const app = express()
    app.use(express.json())
    app.use(express.urlencoded({
        extended: true
    }))

   
    
    await createConnection()

    app.get("/", (_, res) => {
        //@ts-ignore
        res.send("Hello Server")
    })
    app.use(BugRoutes)
    app.use(AuthRoutes)
    app.get("/currentUser", authentificateToken, (req, res) => {
        //@ts-ignore
        res.send(req.user.user)
    })


    app.listen(PORT, () => {
        console.log(`http://localhost:${PORT}`)
    })
}

main().catch(e => console.log('Error in main: ', e))