package com.example.reyhan.polyglot;

/**
 * Created by Reyhan on 10/21/2016.
 */

/*******
 * Class to store image id and language source code
 */
public class Language {
    private String sourcecode;
    private Integer image;

    public Language(String sourcecode, Integer image)
    {
        this.sourcecode = sourcecode;
        this.image = image;
    }

    public String getsourcecode()
    {
        return  sourcecode;
    }

    public Integer getimage()
    {
        return image;
    }
}
