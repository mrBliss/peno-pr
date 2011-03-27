package pcpanic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.swing.JTextPane;



public class StreamPane extends JTextPane {

    private HashMap<InputStream,PollThread> map;
    FileWriter fstream;

    public StreamPane() {
        map = new HashMap<InputStream,PollThread>();
		try {
			fstream = new FileWriter("robotlog.txt", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    public void traceIStream(InputStream p) {
        PollThread t = map.get(p);
        if (t != null) {
            t.stopThread();
        }
        t = new PollThread(p);
        t.start();
        map.put(p, t);
    }

    public void stopITrace(InputStream p) {
        PollThread t = map.get(p);
        if (t != null) {
            t.stopThread();
            t = null;
        }

    }

    public void append(String t){
        setText(getText()+t);
        try {
			fstream.write(t);
			fstream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    private class PollThread extends Thread {

        private InputStream stream;
        private boolean running;

        public PollThread(InputStream stream) {
            this.stream = stream;
            running = true;
        }

        @Override
        public void run() {
            String line = "";
            while (running) {
                try {

                    char ch = (char) stream.read();
                    if (ch != (char) -1) {
                        //append(""+ch);
                        //setCaretPosition(getDocument().getLength());
                        if(ch == '\n'){
                            if(GUI.ts != null && line.startsWith("Turtle")){
                                GUI.ts.moveTurtle(line.charAt(7));
                            }

                            append(line+"\n");

                            line = "";
                        }else{
                           line+=ch;
                           //System.out.println("wordTrace: "+line);
                        }
                    }
                } catch (IOException ex) {
                    System.err.println("exception in NXT ontvangen");
                }
            }
        }

        
        public void stopThread(){
            running = false;
        }
    }
}
