package com.asm.local;

import java.math.BigInteger;

public class PrivateMethodAnalysis {
    public void a() {
        c();
    }
    public void b() {
        c(0);
    }
    private PrivateMethodAnalysis c(){
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
    void g(boolean aa, double d, boolean b, char c, int o , long s, BigInteger a) {
        a();
        b();
        d();
        new PrivateMethodAnalysis2().d();
    }

}
