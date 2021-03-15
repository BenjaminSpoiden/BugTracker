import { BaseEntity, Column, CreateDateColumn, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, UpdateDateColumn } from "typeorm";
import { User } from "./User";

@Entity("bug")
export class Bug extends BaseEntity {


    @PrimaryGeneratedColumn()
    id: string

    @Column()
    name: string

    @Column()
    details: string

    @Column()
    version: string

    @Column()
    priority: number

    @ManyToOne(() => User, user => user.bugs)
    @JoinColumn({name: "creatorId"})
    creator: User

    @Column({nullable: true})
    creatorId?: string
    
    @Column({default: false})
    is_completed: boolean

    @Column({nullable: true, default: null})
    resolution?: string

    @CreateDateColumn()
    created_at: Date

    @UpdateDateColumn()
    updated_at: Date
}