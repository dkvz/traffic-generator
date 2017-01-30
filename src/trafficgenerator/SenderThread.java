
package trafficgenerator;

import java.net.*;
import java.io.*;
import java.util.Date;

public class SenderThread extends Thread {

    private MainFrame frm;
    private int port;
    private int portDone = 0;
    private int from;
    private int to;
    private InetAddress address;
    private int packSize = 1024;
    private long maxCount;
    // Single destination port...
    private int dstPort = 4000;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private boolean terminate = false;
    private Date started = null;
    private Date ended = null;
    private int loopCount = 1;
    private int totalPackets = 0;

    public SenderThread(String name, MainFrame frm, InetAddress address, int packSize, long maxCount, int from, int to, int port) {
        super(name);
        this.address = address;
        this.frm = frm;
        this.from = from;
        this.port = port;
        this.to = to;
        this.packSize = packSize;
        this.maxCount = maxCount;
        byte[] buf = new byte[packSize];
        for (int x = 0; x < packSize; x++) {
            buf[x] = Byte.MAX_VALUE;
        }
        packet = new DatagramPacket(buf, buf.length, address, port);
    }

    public SenderThread(String name, MainFrame frm, InetAddress address, int packSize, long maxCount, int from, int to, int port, int dstPort) {
        super(name);
        this.address = address;
        this.frm = frm;
        this.from = from;
        this.port = port;
        this.to = to;
        this.packSize = packSize;
        this.maxCount = maxCount;
        this.dstPort = dstPort;
        byte[] buf = new byte[packSize];
        for (int x = 0; x < packSize; x++) {
            buf[x] = Byte.MAX_VALUE;
        }
        packet = new DatagramPacket(buf, buf.length, address, dstPort);
        this.frm.append("INFO", true, this.getName() + " initialized to send to address " + address.getHostAddress() + " port " + dstPort + ".");
        Thread.yield();
    }

    @Override
    public void run() {
        try {
            started = new Date();
            int portCount = to - from;
            int loops = 0;
            while (!terminate && loops < loopCount) {
                this.frm.append("INFO", true, this.getName() + " started loop " + Integer.toString(loops + 1)  + ".");
                while (!terminate && portDone < portCount) {
                    int count = 0;
                    socket = new DatagramSocket(this.port);
//                    this.frm.append("INFO", true, "Thread " + this.getName() + " Sending loop started from port " + port + " to server port " + port + ".");
                    while (!terminate && count < maxCount) {
                        socket.send(packet);
                        count++;
                    }
                    totalPackets += count;
                    count = 0;
                    portDone++;
                    port++;
                    if (port > to) {
                        port = from;
                    }
                    socket.close();
                }
                loops++;
            }
        } catch (SocketException ex) {
            this.frm.append("ERROR", true, "Thread " + this.getName() + " Sending socket for port " + port + " could not be initiated : " + ex.toString());
        } catch (IOException ex) {
            this.frm.append("ERROR", true, "Thread " + this.getName() + " There was an IO error for port " + port + " exception : " + ex.toString());
        }
        ended = new Date();
        this.frm.append("INFO", true, "Thread " + this.getName() + " ended. " + this.totalPackets + " packets sent.");
        terminate = true;
        frm.notifyThreadEnd();
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
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the packSize
     */
    public int getPackSize() {
        return packSize;
    }

    /**
     * @return the maxCount
     */
    public long getMaxCount() {
        return maxCount;
    }

    /**
     * @return the packet
     */
    public DatagramPacket getPacket() {
        return packet;
    }

    /**
     * @return the socket
     */
    public DatagramSocket getSocket() {
        return socket;
    }

    /**
     * @return the address
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    /**
     * @return the from
     */
    public int getFrom() {
        return from;
    }

    /**
     * @return the terminate
     */
    public boolean isTerminate() {
        return terminate;
    }

    /**
     * @param terminate the terminate to set
     */
    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
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
     * @return the loopCount
     */
    public int getLoopCount() {
        return loopCount;
    }

    /**
     * @param loopCount the loopCount to set
     */
    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount + 1;
    }

    /**
     * @return the totalPackets
     */
    public int getTotalPackets() {
        return totalPackets;
    }

}
