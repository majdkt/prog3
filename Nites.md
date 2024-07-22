- MAX_TOTAL_CAPACITY should be given in configuration of CLIMAIN 
- Contracture from Admin gets max capacity from program arguments
-     public static void main(String[] args) Long.parseLong{ arg[0]} something like that
- There must be a methode to create an uploader
- you can have a list of uploaders who don't have any media
- You have first to create an Uploader, the uploader is allowed to be chosen and to upload MediaContent
- Befehlssatz for CLI
  • :c Wechsel in den Einfügemodus ( Create )
  • :d Wechsel in den Löschmodus ( Delete )
  • :r Wechsel in den Anzeigemodus ( Read )
  • :u Wechsel in den Änderungsmodus ( Update )
  • :p Wechsel in den Persistenzmodus 
- Example:
• :c
  Alice
  Audio Alice Music 1200 2,05 44100
  AudioVideo Alice Review,Music 7600 2,25
  :r
  content Audio
  tag i
  uploader
