
package trafficgenerator;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 *
 * @author Alain
 */
public class ServerThread extends Thread {

    public static final int BUF_SIZE = 8192;
    
    private int port;
    private MainFrame frm;
    private DatagramSocket socket;
    private boolean terminate = false;
    private long receivedBytes = 0;
    private long maxBytes = 1000;
    private Date started = null;
    private Date ended = null;
    private boolean noLimit = false;
    private boolean firstReceived = false;

    public ServerThread(String name, MainFrame frm, int port, long maxBytes) {
        super(name);
        this.port = port;
        this.frm = frm;
        this.maxBytes = maxBytes;
        if (maxBytes <= 0) {
            this.noLimit = true;
        }
    }

    @Override
    public void run() {
        try {
            byte [] buf = new byte[ServerThread.BUF_SIZE];
            this.socket = new DatagramSocket(port);
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            this.frm.append("INFO", false, this.getName() + " started on port " + port + ".");
            this.started = new Date();
            while (!terminate && (noLimit || (this.receivedBytes < this.maxBytes))) {
                socket.receive(packet);
                if (!firstReceived) {
                    firstReceived = true;
                    this.started = new Date();
                }
                this.receivedBytes += packet.getLength();
            }
            this.socket.close();
        } catch (SocketException ex) {
            this.frm.append("ERROR", false, "Thread " + this.getName() + " Socket stopped brutally for port " + port + " : " + ex.toString());
        } catch (IOException ex) {
            this.frm.append("ERROR", false, "Thread " + this.getName() + " IO error : " + ex.toString());
        }
        this.ended = new Date();
        terminate = true;
        this.frm.notifyServerThreadEnd();
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the frm
     */
    public MainFrame getFrm() {
        return frm;
    }

    /**
     * @param frm the frm to set
     */
    public void setFrm(MainFrame frm) {
        this.frm = frm;
    }

    /**
     * @return the terminate
     */
    public boolean isTerminate() {
        return terminate;
    }

    /**
     * @return the receivedBytes
     */
    public long getReceivedBytes() {
        return receivedBytes;
    }

    /**
     * @return the started
     */
    public Date getStarted() {
        return started;
    }

    /**
     * @return the ended
     */
    public Date getEnded() {
        return ended;
    }

    /**
     * @param terminate the terminate to set
     */
    public void setTerminate(boolean terminate) {
        this.socket.close();
        this.terminate = terminate;
    }

    /**
     * @return the maxBytes
     */
    public long getMaxBytes() {
        return maxBytes;
    }

    /**
     * @param maxBytes the maxBytes to set
     */
    public void setMaxBytes(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    /**
     * @return the noLimit
     */
    public boolean isNoLimit() {
        return noLimit;
    }

    /**
     * @param noLimit the noLimit to set
     */
    public void setNoLimit(boolean noLimit) {
        this.noLimit = noLimit;
    }
    

}
