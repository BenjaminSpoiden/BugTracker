import { Router } from "express"
import { createUser, loginUser, logoutUser } from "../controllers/AuthController"

const router = Router()

router.post("/signup", createUser)
router.post("/login", loginUser)
router.get("/logout", logoutUser)

export default router