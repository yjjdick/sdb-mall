package io.sdb.common.entity;


import lombok.Data;

import java.util.List;


abstract class P{
    public void print(){
        System.out.println("pppppppppppp");
    }
}


class C extends P{
    public void print(){
        System.out.println("ccccccccccccccc");
    }
}

class A<T extends P> {
    public void print(T param){
        param.print();
    }
}

@Data
public abstract class Node<T> {

    private Long id;
    private Long parentId;
    private List<T> children;

    public Node() {
    }

    public Node(Long id, Long pid) {
        super();
        this.id = id;
        this.parentId = pid;
    }


    public static void main(String[] args) {
        A a = new A<C>();
        a.print(new C());

    }
}
