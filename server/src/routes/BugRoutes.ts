import { Router } from "express";
import { authentificateToken } from "../middleware/AuthMiddleware";
import { addBug, deleteBug, getBugs, updateBug } from "../controllers/BugController";


const router = Router()

router.get("/bugs", getBugs)
router.post("/addBug", authentificateToken, addBug)
router.delete("/deleteBug/:id", authentificateToken, deleteBug)
router.put("/updateBug/:id", updateBug)

export default router