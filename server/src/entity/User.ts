import { IsEmail, Length } from "class-validator";
import { BaseEntity, BeforeInsert, Column, CreateDateColumn, Entity, OneToMany, PrimaryGeneratedColumn, UpdateDateColumn } from "typeorm";
import argon2 from "argon2"
import { Bug } from "./Bug";

@Entity()
export class User extends BaseEntity {

    @PrimaryGeneratedColumn()
    id: string

    @Column()
    @IsEmail()
    email: string

    @Column()
    @Length(3, 16)
    username: string

    @Column()
    password: string

    @OneToMany(() => Bug, bug => bug.creator)
    bugs: Bug[]

    @CreateDateColumn()
    created_at: Date

    @UpdateDateColumn()
    updated_at: Date

    @BeforeInsert()
    async onHashPassword() {
        this.password = await argon2.hash(this.password)
    }

    static async onLogin(email: string, password: string) {

        const user = await this.findOne({ email })
        if(!user) throw Error("The email you entered doesn't exist.")

        const comparePassword = await argon2.verify(user.password, password)
        if(!comparePassword) throw Error("The passwords did not match") 

        return user
    }
}