package pt.tecnico.mydrive.domain;

import java.util.Set;
import org.jdom2.Element;
import org.jdom2.Document;
import java.util.Set;
import pt.tecnico.mydrive.exception.*;

public class Directory extends Directory_Base {
    
	//construtor
    //NETO: o file preenche os campos das permissoes pois tem o user
	public Directory(String name, int id, User user, Directory father) {//o mydrive tem de fazer controlo dos ids
        super();
		//super(name, id, user); superclasse nao construida
        setFatherDirectory(father);
        setSelfDirectory(this);
    }
	
	public Directory(String name, int id, User user) {//o mydrive tem de fazer controlo dos ids
		super();
        //super(id, name, user); superclasse nao construida
        setFatherDirectory(this);
        setSelfDirectory(this);
    }
	
	public void createDir(String name, int id, User user) throws FileAlreadyExistsException {
		try {
			search(name);
			throw new FileAlreadyExistsException(name);
		} 
		catch (NoSuchFileException e) {
			Directory d = new Directory(name, id, user, this);
			addFiles(d);
		}
		
	}

	public void remove(String name) throws NoSuchFileException{
		File f = search(name);
		f.remove();
		removeFiles(f);
	}
	
	public void remove() {
		Set<File> files = getFiles();
		for (File f: files) {
   	 		f.remove();
   	 		removeFiles(f);
   	 	}
	}
	
	public File get(String name) throws NoSuchFileException, FileNotDirectoryException{
		if (name.equals("..")) {
			return getFatherDirectory();
		}
		else if (name.equals(".")) {
			return getSelfDirectory();
		}
		else {
			File f = search(name);
   	 		return f;
		}
	}
	
	public File search(String name) throws NoSuchFileException{	
		Set<File> files = getFiles();
		
		for (File f: files) {
   	 		if (f.getName().equals(name)) {
   	 			return f;
   	 		}
		}
		throw new NoSuchFileException(name);
	}
	
	//lista o conteudo da diretoria
	public String ls(){
		String output="";
		Set<File> files = getFiles();
   	 	output+=getFatherDirectory().toString()+getSelfDirectory().toString();
   	 	for (File f: files){
   	 		output+= f.toString();
   	 	}
		return output;
	}
	
	//devolve a descricao da diretoria
	public String toString(){
		String s = "Directory ";
		s+="Dimension: "+dimension();
		s+=super.toString(); //logo se vê
		return s;
	}
	
	//devolve a dimensao da diretoria
	public int dimension(){
		return 2 + getFiles().size();
	}
	
	public void accept(Visitor v) throws FileNotDirectoryException{
		v.execute(this);
	}
	
	public void XMLExport(Element element_mydrive){
        Element element = new Element ("dir");
        //element.setAttribute("id", getId());
        
        element = new Element ("path");
        //element.setText(getPath());
        
        element = new Element ("name");
        element.setText(getName());
        
        element = new Element ("owner");
        element.setText(getOwner().getUsername());
        
        element = new Element ("perm");
        //element.setText(getPerm());
        
        element_mydrive.addContent(element);
        for (File f: getFiles()){
            f.xmlExport(element_mydrive);
        }
    }

}
