import { Router } from "express";
import { addBug, deleteBug, getBugs, updateBug } from "../controllers/BugController";


const router = Router()

router.get("/bugs", getBugs)
router.post("/addBug", addBug)
router.delete("/deleteBug/:id", deleteBug)
router.put("/updateBug/:id", updateBug)

export default router