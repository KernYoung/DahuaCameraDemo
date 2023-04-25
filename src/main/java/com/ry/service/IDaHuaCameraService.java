package com.ry.service;

import java.io.IOException;

public interface IDaHuaCameraService {

    boolean loginWithHighSecurity(String ip, int port, String username, String password);

    boolean logout();

    boolean push() throws IOException;
}

