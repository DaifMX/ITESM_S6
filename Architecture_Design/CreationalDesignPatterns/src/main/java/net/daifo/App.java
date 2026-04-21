package net.daifo;

import net.daifo.singleton.Singleton;
import net.daifo.builder.Builder;
import net.daifo.factorymethod.FactoryMethod;
import net.daifo.abstractfactory.AbstractFactory;
import net.daifo.prototype.Prototype;

public class App {
    public static void main(String[] args) {
        Singleton.demo();
        Builder.demo();
        FactoryMethod.demo();
        AbstractFactory.demo();
        Prototype.demo();
    }
}
