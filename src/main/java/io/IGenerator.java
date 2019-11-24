package io;

import javax.swing.filechooser.FileNameExtensionFilter;

public interface IGenerator {
    
    //TODO JavaDoc
    public String generate();
    public FileNameExtensionFilter getFileNameExtensionFilter();
}
