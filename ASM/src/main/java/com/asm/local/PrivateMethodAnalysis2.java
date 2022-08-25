package com.asm.local;

import java.math.BigInteger;

public class PrivateMethodAnalysis2 {
    public void a() {
        c();
    }
    public void b() {
        c(0);
    }
    private PrivateMethodAnalysis2 c(){
        System.out.println("hello");
        return null;
    }

    public void d() {
        c();
    }
    private void c(int a){
        System.out.println("hello");
    }
    public void f(Long aa) {
       a();
       b();
       d();
    }
    public void g(boolean aa, double d, boolean b, char c, int o , long s, BigInteger a) {
        a();
        b();
        d();
    }

}
