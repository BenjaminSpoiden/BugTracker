import { BaseEntity, Column, CreateDateColumn, Entity, PrimaryGeneratedColumn, UpdateDateColumn } from "typeorm";

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

    @Column({default: false})
    is_completed: boolean

    @Column({nullable: true, default: null})
    resolution?: string

    @CreateDateColumn()
    created_at: Date

    @UpdateDateColumn()
    updated_at: Date
}