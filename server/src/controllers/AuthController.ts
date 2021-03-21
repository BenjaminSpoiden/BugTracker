import { validate } from "class-validator";
import { User } from "../entity/User";
import { Request, Response } from "express"
import jwt from "jsonwebtoken"

const createToken = (user: User) => {
    //@ts-ignore
    return jwt.sign({ user }, process.env.ACCESS_TOKEN_SECRET, {
        expiresIn: 365 * 24 * 60 * 60
    })
}

export const createUser = async (req: Request, res: Response) => {
    const {email, username, password} = req.body

    try {
        const user = User.create({email, username, password})
        const errors = await validate(user)
        if(errors.length > 0) {
            res.status(401).send(errors[0].constraints)
        }else {
            await user.save()
            const accessToken = createToken(user)
            res.status(201).json({ accessToken })
        }
        
        res.status(201).send("User created")
    }catch (e) {
        res.status(401).send(e)
    }
}

export const loginUser = async (req: Request, res: Response) => {
    const {email, password} = req.body

    try {
        const user = await User.onLogin(email, password)  

        const accessToken = createToken(user)
        res.json({ accessToken })
    }catch(e) {
        res.status(401).send(e.message)
    }
}

export const logoutUser = async (req: Request, _: Response) => {
    //@ts-ignore
    req.user = null
}