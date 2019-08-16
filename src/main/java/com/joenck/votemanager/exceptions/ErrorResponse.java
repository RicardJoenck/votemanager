package com.joenck.votemanager.exceptions;


public class ErrorResponse {
    private int status;
    private String messagem;

    public ErrorResponse()
    {
        super();
    }
    public ErrorResponse(int status, String messagem)
    {
        super();
        this.status = status;
        this.messagem = messagem;
    }

    public int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status = status;
    }
    public String getMessagem()
    {
        return messagem;
    }
    public void setMessagem(String messagem)
    {
        this.messagem = messagem;
    }
    @Override
    public String toString()
    {
        return "ErrorResponse [status=" + status + ", message=" + messagem + "]";
    }
}

