/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.bd.VideoBD;
import com.mynor.manejador.cines.api.dtos.VideoDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosVideo;
import com.mynor.manejador.cines.api.modelo.Video;

/**
 *
 * @author mynordma
 */
public class VideoServicio {
    
    private final BaseDeDatos<Video, FiltrosVideo> VIDEO_BD;
    
    public VideoServicio(){
        VIDEO_BD = new VideoBD();
    }

    public VideoDTO crearVideo(VideoDTO videoDTO) throws AccesoDeDatosException {
        Video video = new Video();
        video.setLink(videoDTO.getLink());
        
        try(Transaccion t = new Transaccion()){
            Video videoCreado = VIDEO_BD.crear(video, t.obtenerConexion());
            t.commit();
            
            VideoDTO videoCreadoDTO = new VideoDTO();
            videoCreadoDTO.setId(String.valueOf(videoCreado.getId()));
            videoCreadoDTO.setLink(videoCreado.getLink());
            return videoCreadoDTO;
        }
    }
    
}
