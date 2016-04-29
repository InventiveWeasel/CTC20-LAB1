package tabela;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Tabela {
	
	public int tamGrupo;
	char Tabela[][];
	String arquivo;
        int count;
	
	public Tabela(String fname){
		tamGrupo=0;
                arquivo = fname;
                count = 0;
                
	}
	
        //Verifica se a tabela lida eh um grupo
	public boolean ehGrupo(){
		boolean ehGrupo = false;
		if(pFechamento(Tabela,tamGrupo)){
			System.out.println("É fechado");
			if(pInversa(Tabela,tamGrupo)){
				System.out.println("É inversa");
				if(pAssociativa(Tabela,tamGrupo)){
					System.out.println("É associativa");
					ehGrupo=true;
				}			
			}
		}
		return ehGrupo;
	}
        
        //Verifica se a dada mariz de adjacencias representa um sugrupo
        public boolean ehSubgrupo(List elements, char subgroup[][]){
            boolean ehGrupo = false;
            boolean hasNeutro = false;
            for(int i = 0; i<elements.size(); i++){
                if((char) elements.get(i) == '1')
                    hasNeutro = true;
            }   
            if(hasNeutro){
                if(pFechamento(subgroup,elements.size())){
                    if(pInversa(subgroup,elements.size())){
                        ehGrupo = true;
                    }
                }
            }else
                return false;
            return ehGrupo;
        }
	
        //calcula o tamanho da tabela (numero de elementos)
	public void  verTamanhoTabela(String arquivo) throws IOException{
		FileInputStream file = new FileInputStream(arquivo);
		Scanner s = new Scanner(file);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			tamGrupo++;
		}
		s.close();
		file.close();
		System.out.printf("O tamanho da tabela é %d\n",tamGrupo);
	}
	
        //funcao usada no momento em que lemos a tabela pela primeira vez
        //Eh responsavel por "inicializar seus componentes.
	public void readTabela()throws IOException {
            try{
                verTamanhoTabela(arquivo);
                Tabela = new char[tamGrupo][tamGrupo];
                FileInputStream file = new FileInputStream(arquivo);
                Scanner s = new Scanner(file);		
                int i=0;
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    for (int k=0 ;k<tamGrupo; k++){
                        Tabela[i][k] = line.charAt(k);
                    }
                    i++;
                }
                s.close();
                file.close();
            }catch (IOException e) {
                System.out.println("Deu ruim");
            }
	}
	
        //Verifica a validade da propriedade de fechamento dada uma
        //matriz de adjacencicas
	private boolean pFechamento(char Subgrupo[][],int size){
		boolean valeFechamento = true;
		boolean igualAAlgumElemento = false;
		for(int i=0;i<size && valeFechamento ;i++){
			for(int k=0;k<size;k++){
				igualAAlgumElemento = false;
				for(int j=0; j<size;j++){
					if(Subgrupo[i][k]== Subgrupo[j][0])
						igualAAlgumElemento = true;
				}
				if (igualAAlgumElemento == false)
					valeFechamento = false;
			}
		}	
		return valeFechamento;
	}
	
	//Realiza uma operação entre dois operandos de uma tabela
	private int operacaoXY (char Subgrupo[][], int size, int X, int Y)
	{
		int indice=-1;;
		for(int i=0;i<size;i++){
			if(Subgrupo[X][Y] == Subgrupo[i][0])
				indice = i;
		}
		return indice;
	}
	
        //Verifica a propriedade associativa dada uma matriz de adjacencias
	private boolean pAssociativa(char Subgrupo[][],int size ){
		boolean valeAssociativa = true;
		int elemento1, elemento2, elemento3,op1, op2;
		//Para esta função, consideramos que o Subgrupo é fechado
		for(elemento1 = 0 ; elemento1 < size && valeAssociativa; elemento1++){
			for(elemento2 = 0 ; elemento2 < size && valeAssociativa ; elemento2++){
				for(elemento3 = 0 ; elemento3 < size && valeAssociativa; elemento3++ ){
					op1 = operacaoXY(Subgrupo, size, elemento1, elemento2);
					op2 = operacaoXY(Subgrupo, size, elemento2, elemento3);
					if(operacaoXY(Subgrupo,size,op1,elemento3)!= operacaoXY(Subgrupo,size,elemento1,op2)){
						valeAssociativa = false;
						//System.out.printf("Não é associativa para os elementos %d %d %d\n", elemento1,elemento2, elemento3);
					}
				}
			}	
		}
		return valeAssociativa;
	}
	
        //Verifica a propriedade inversa dado uma matriz de adjacências
	private boolean pInversa (char Subgrupo[][], int size){
		boolean valeInversa = true;
		boolean elementoNeutro = false;
		//Tomando tabelas a partir de dois elementos, já contendo o elemento neutro 
		for(int i=1; i<size && valeInversa; i++){
			elementoNeutro = false;
			for(int k=1; k<size && !elementoNeutro; k++){
				if(Subgrupo[i][k] == '1' && Subgrupo[k][i] == '1'){
					elementoNeutro=true;
				}
			}
			if(!elementoNeutro)
			{
				valeInversa = false;
			}
		}
		return valeInversa;
		
	}
	
        public char[] generateGroupString(){
            String sorted;
            char elements[] = new char[this.tamGrupo];
            for(int i = 0; i < this.tamGrupo; i++){
                elements[i] = this.Tabela[i][0];
            }
            Arrays.sort(elements);
            return elements;
        }
        
        //Dada uma lista de elementos, gera a tabela do grupo
        public char[][] generateSubgroupMatrix(List elements){
            int tamSub;
            char matrix[][];
            int pos[];
            int p = 0;
            int elemIter;
            
            tamSub = elements.size();
            matrix = new char[tamSub][tamSub];
            pos = new int[tamSub];
            int posIt = 0;
            for(int i = 0; i<tamSub; i++){
                for(int j = 0; j < this.tamGrupo; j++){
                    if(this.Tabela[j][0] == (char) elements.get(i)){
                        pos[posIt] = j;
                        posIt++;
                        for(int x = 0; x< pos.length; x++){
                            for(int y=0; y < pos.length; y++){
                                matrix[y][x] = this.Tabela[pos[y]][pos[x]];
                            }
                        }
                        p = 0;
                    }
                }
            }
            return matrix;
        }
        
        public void createGraphFile(char[] elements) throws IOException, InterruptedException{
            ArrayList subGroups = new ArrayList();
            //Aqui geramos os subgrupos
            for (long i = 0, max = 1 << elements.length; i < max; i++) {
			// we create a new subset
			List newSet = new ArrayList();
			for (int j = 0; j < elements.length; j++) {
				// check if the j bit is set to 1
				int isSet = (int) i & (1 << j); 				
                                if (isSet > 0) {
					// if yes, add it to the set
					newSet.add(elements[j]);
				}
			}
                        char aux[] = new char[newSet.size()];
                        char subgroup[][] = generateSubgroupMatrix(newSet);
                        //Verificando se é grupo
                        boolean k = ehSubgrupo(newSet, subgroup);
                        if(ehSubgrupo(newSet, subgroup)){
                                //Precisamos tranformar os elementos do new set para strings
                                List auxNew = new ArrayList();
                                String value="";
                                String group="";
                                for(int j = 0; j < newSet.size();j++){
                                    value = value + newSet.get(j);
                                    auxNew.add(value);
                                    value = "";
                                    group = group+newSet.get(j);
                                }
                                subGroups.add(group);
                        }
            }
            boolean adj[][] = generateAdjMatrix(subGroups);
            generateInputFile(adj, subGroups);
            System.out.println("asa");
        }
        
        //Gera a matriz de adjacências do grupo
        public boolean[][] generateAdjMatrix(ArrayList subgroups){
            int nSub = subgroups.size();
            boolean adj[][] = new boolean[nSub][nSub];
            for(int i = 0; i<nSub;i++)
                for(int j = 0; j<nSub;j++)
                    adj[i][j] = false;
            ArrayList<String> element = new ArrayList<String>();
            ArrayList<String> aux = new ArrayList<String>();
            ArrayList subs[];
            String value = "";
            //Ordenando os elementos da lista
            Collections.sort(subgroups);
            
            //Percorre-se elemento a elemento
            for(int i = 0; i<nSub; i++){
                //Obtemos uma lista cujos objetos são os caracteres da string do subgrupo
                String e = (String) subgroups.get(i);
                for(int j=0; j<e.length();j++){
                    value = value+e.charAt(j);
                    element.add(value);
                    value = "";
                }
                //Para um mesmo elemento, fazemos o mesmo para os outros
                for(int j = 0; j <nSub ;j++){
                   String a = (String) subgroups.get(j);
                   for(int k = 0; k < a.length();k++){
                       value = value+a.charAt(k);
                       aux.add(value);
                       value = "";
                   }
                   int k = 1;
                   //verifica-se se os conjuntos sao iguais
                   if(!(aux.containsAll(element) && element.containsAll(aux))){
                       //Vemos a direcao da ligacao entre os elementos
                       if(aux.containsAll(element))
                            adj[i][j] = true;
                        else if(element.containsAll(aux))
                            adj[j][i] = true;
                   }
                   aux.clear();
                   
                }
                element.clear();
            }
            //Resolvendo a transitividade
            subs = new ArrayList[nSub];
            value = "";
            //Aqui geramos um vetor de listas com os conjuntos de caracteres dos subgrupos
            for(int i = 0; i<nSub; i++){
                subs[i] = new ArrayList();
                //Obtemos uma lista cujos objetos são os caracteres da string do subgrupo
                String e = (String) subgroups.get(i);
                for(int j=0; j<e.length();j++){
                    value = value+e.charAt(j);
                    subs[i].add(value);
                    value = "";
                }
            }
            for(int i = 0; i<nSub; i++){
                boolean isConnected[] = new boolean[nSub];
                for(int j = 0; j < nSub; j++){
                    if(adj[i][j] == true){
                        isConnected[j] = true;
                    }else{
                        isConnected[j] = false;
                    }
                }
                //Iterando sobre o vetor de conexões
                for(int j = 0; j < nSub; j++){
                    for(int k = 0; k < nSub; k++){
                        if(k!=j){
                            if(isConnected[j] && isConnected[k]){
                                if(subs[j].containsAll(subs[k])){
                                    adj[i][j] = false;
                                }else if(subs[k].containsAll(subs[j])){
                                    adj[i][k] = false;
                                }
                            }
                        }
                    }
                }
            }
            int k =2;
            return adj;
        }
        
        //Gera o arquivo a ser executado pelo GraphViz
        public void generateInputFile (boolean adj[][], ArrayList subGroups) throws IOException{
            int nAdj = subGroups.size();
            String output = "input"+Integer.toString(this.count)+".dot";
            String a1, a2;
            
            PrintWriter writer = new PrintWriter(output, "UTF-8");
            writer.write("digraph mygraph{\r\n");
            
            for(int i = 0; i < nAdj; i++){
                for(int j = 0; j < nAdj; j++){
                    if(adj[i][j] == true){
                        a1 = (String) subGroups.get(i);
                        a2 = (String) subGroups.get(j);
                        a1 = subFormat(a1);
                        a2 = subFormat(a2);
                        System.out.println(a1+ " -> " +a2);
                        writer.write(a1+ " -> " +a2+ " \r\n");
                    }
                }
            }
            writer.write("}");
            writer.close();
        }
        
        //Formata as strings dos subgrupos para serem impressas no arquivo .dot
        private static String subFormat(String set) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"{");
		// for each integer
		for (int i = 0; i < set.length(); i++) {
			String value = "";
                        value = value+set.charAt(i);
			buffer.append(value);
			if (i != set.length() - 1) {
				buffer.append(",");
			}
		}
		buffer.append("}\"");
		return buffer.toString();
	}
            
        
	public static void main(String args[]) throws IOException, InterruptedException{
            char[] elements;
            for(int i = 1; i<=8; i++){
                Tabela Grupo = new Tabela("table "+Integer.toString(i)+".txt");
                Grupo.readTabela();
                Grupo.count=i;
                if(Grupo.ehGrupo()){
                    elements = Grupo.generateGroupString();
                    Grupo.createGraphFile(elements);
                    boolean ok = Grupo.ehGrupo();
                    System.out.println("ok");
                }
                else
                    System.out.println("A tabela "+Integer.toString(i)+" nao eh grupo.");
                
            }
	}
	
}

