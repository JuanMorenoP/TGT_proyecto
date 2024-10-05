package com.example.tgt_proyecto;

public class Cliente {
    private final int cli_id;
    private final String cli_nombre;

    public Cliente(int cli_id, String cli_nombre) {
        this.cli_id = cli_id;
        this.cli_nombre = cli_nombre;
    }

    public int getCli_id() {
        return cli_id;
    }

    public String getCli_nombre() {
        return cli_nombre;
    }
}
