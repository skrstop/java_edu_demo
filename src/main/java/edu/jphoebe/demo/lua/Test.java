package edu.jphoebe.demo.lua;

import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua54;

/**
 * @author 蒋时华
 * @date 2022-08-03 12:32:08
 */
public class Test {

    public static void main(String[] args) {
        try (Lua lua = new Lua54()) {
            lua.openLibraries();

        }
    }

}
