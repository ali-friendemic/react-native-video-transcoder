/**
 * This exposes the native CalendarModule module as a JS module. This has a
 * function 'createCalendarEvent' which takes the following parameters:
 *
 * 1. String name: A string representing the name of the event
 * 2. String location: A string representing the location of the event
 */
import {NativeModules} from 'react-native';
const {TranscoderModule} = NativeModules;
interface TranscoderInterface {
  getByteThumbnail(
    name: string,
    path: string,
    quality: number,
    callback?: (path: string) => void,
  ): void;
  getFileThumbnail(
    name: string,
    path: string,
    quality: number,
    callback?: (path: string) => void,
  ): void;
  cancelCompression(callback?: (value: Boolean) => void): void;
  compressVideo(
    name: string,
    path: string,
    quality: Number,
    deleteOrigin: Boolean,
    includeAudio?: Boolean,
    startTime?: Number,
    frameRate?: Number,
    callback?: (json: string | null) => void,
  ): void;
}
export default TranscoderModule as TranscoderInterface;
