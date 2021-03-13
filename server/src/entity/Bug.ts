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

    @CreateDateColumn()
    created_at: Date

    @UpdateDateColumn()
    updated_at: Date

    @Column({default: false})
    is_completed: boolean
    
}