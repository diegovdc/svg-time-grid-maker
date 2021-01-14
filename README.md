# Hacedor de grids temporales svg

Genera grids con la siguientes características:

- 2 minutos por imagen
- 5 segundos por intervalo de tiempo
- proporciones 110:77 (doble carta)

## Uso

```clj
;; crea la imagen correspondiente al minuto 0 a 2 con el nombre `0-2.svg`
(make-svg 0)
```

Las imágenes se guardan en la carpeta `output/`

El `index.html` puede ser abierto en un navegador para visualizar las imagenes.
