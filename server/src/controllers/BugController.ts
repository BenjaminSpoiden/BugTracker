import {Request, Response} from "express"
import { Bug } from "../entity/Bug"

export const getBugs = async (_: Request, res: Response) => {
    try {
        const bugs = await Bug.find()
        res.status(200).send(bugs)
    } catch(e) {
        res.status(400).send({message: e.message})
    }
}

export const addBug = async (req: Request, res: Response) => {
    const data = req.body as Bug

    try {
        const addedBug = await Bug.create(data).save()
        res.status(200).send(addedBug)
    }catch(e) {
        res.status(400).send({message: e.message})
    }
}

export const deleteBug = async (req: Request, res: Response) => {

    const { id } = req.params
    try {
        await Bug.delete({ id })
        res.status(200).send({message: "Bug successfully deleted"})
    }catch(e) {
        res.status(400).send({message: e.message})
    }
}

export const updateBug = async (req: Request, res: Response) => {
    const { id } = req.params
    const data = req.body as Bug

    try{
        await Bug.update({ id }, data)
        res.status(200).send({message: "Bug succesfully modified"})
    }catch(e) {
        res.status(400).send({message: e.message})
    }
}