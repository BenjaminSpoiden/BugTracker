import { Request, Response, NextFunction } from "express";
import jwt from "jsonwebtoken"


export const authentificateToken = (req: Request, res: Response, next: NextFunction) => {
    const authHeader = req.headers['authorization']
    const token = authHeader && authHeader.split(' ')[1]

    if(!token) return res.status(401).json({
        status:"Failed",
        message: "You are not authorized."
    })
    //@ts-ignore
    jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, user) => {
        if(err) return res.status(403).json({
            status: "Failed",
            message: `You must be logged in to perform this action`
        })
        //@ts-ignore
        req.user = user
        next()
    })

}