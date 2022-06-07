package com.unosquare.carmigo.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;

public class ResourceUtility
{
    public static String generateStringFromResource(final String path)
    {
        try {
            return Resources.toString(Resources.getResource(path), Charsets.UTF_8);
        } catch (IOException ex) {
            return "Cannot retrieve resource entity";
        }
    }

    private ResourceUtility() {}
}
