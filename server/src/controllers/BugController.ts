import {Request, Response} from "express"
import { User } from "../entity/User"
import { getConnection, getConnectionManager } from "typeorm"
import { Bug } from "../entity/Bug"


export const getBugs = async (_: Request, res: Response) => {
    try {
        // const bugs = await getConnection().query(`
        //     SELECT * from "bug"
        //     ORDER BY priority DESC;
        // `)
        const bugs = await getConnection()
            .getRepository(Bug)
            .createQueryBuilder("bug")
            .leftJoinAndSelect("bug.creator", "user")
            .orderBy("bug.priority", "DESC")
            .getMany();
        res.status(200).send(bugs)
    } catch(e) {
        res.status(400).json({
            status: "Failed",
            message: e.message
        })
    }
}

export const addBug = async (req: Request, res: Response) => {
    const data = req.body as Bug
    //@ts-ignore
    const user = req.user.user as User
    console.log("user: ", user)
    try {
        const addedBug = await Bug.create({...data, creatorId: user.id, }).save()
        res.status(200).send(addedBug)
    }catch(e) {
        res.status(400).json({
            status: "Failed",
            message: e.message
        })
    }
}

export const deleteBug = async (req: Request, res: Response) => {

    const { id } = req.params
    try {
        await Bug.delete({ id })
        res.status(200).json({
            status: "Success",
            message: "Bug succesfully deleted."
        })
    }catch(e) {
        res.status(400).json({
            status: "Failed",
            message: e.message
        })
    }
}

export const updateBug = async (req: Request, res: Response) => {
    const { id } = req.params
    const data = req.body as Bug

    try{
        await Bug.update({ id }, {...data})
        res.status(200).json({
            status: "Success",
            message: "Bug successfully modified"
        })
    }catch(e) {
        res.status(400).json({
            status: "Failed",
            message: e.message
        })
    }
}