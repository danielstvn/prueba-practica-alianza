import { Injectable } from '@angular/core';
import { QueryFilter } from '../interfaces/query_filter';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HelpersService {

  constructor() {}

  /**
   * create http params
   * @param filterObject - filter Object
   */
  createHttpParamsString(filterObject: QueryFilter) {
    let dotObject = this.changeObjectToDotNotationFormat(filterObject, null, null);
    let httpParams = new HttpParams();

    for (let key in dotObject) {
      let value = dotObject[key];
      let keyObj = key.replace("criteria.", "");
      keyObj = keyObj.replace("pageable.", "");
      httpParams = httpParams.set(keyObj, value);
    }

    return httpParams;
  }

  /**
   * Transform object
   * @param inputObject
   * @param current
   * @param prefinalObject
   * @returns
   */
  changeObjectToDotNotationFormat(inputObject: any, current: any, prefinalObject: any) {
    const result = prefinalObject ? prefinalObject : {};
    for (let key in inputObject) {
      let value = inputObject[key];
      let newKey = current ? `${current}.${key}` : key;
      if (value && typeof value === "object") {
        this.changeObjectToDotNotationFormat(value, newKey, result);
      } else {
        result[newKey] = value;
      }
    }
    return result;
  }
}
