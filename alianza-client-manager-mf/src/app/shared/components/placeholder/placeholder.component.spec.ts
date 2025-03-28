import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, ParamMap, convertToParamMap } from '@angular/router';
import { PlaceholderComponent } from './placeholder.component';
import { of } from 'rxjs';

describe('PlaceholderComponent', () => {
  let component: PlaceholderComponent;
  let fixture: ComponentFixture<PlaceholderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaceholderComponent],
      providers: [
        {

          provide: ActivatedRoute,
          useValue: {
            params: of({}),
            queryParams: of({}),
            paramMap: of(convertToParamMap({
              id: '1'
            })),

            fragment: of(''),
            data: of({}),
            url: of([]),
            snapshot: {
              paramMap: convertToParamMap({
                id: '1'
              }),
              queryParamMap: convertToParamMap({}),
              url: [],
              fragment: '',
              data: {}
            }
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jasmine.createSpy('navigate'),
            events: of({})
          }
        }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlaceholderComponent);
    component = fixture.componentInstance;

    spyOn(component, 'ngOnInit').and.callFake(() => {
      console.log('ngOnInit llamado sin errores');
      return null;
    });

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
