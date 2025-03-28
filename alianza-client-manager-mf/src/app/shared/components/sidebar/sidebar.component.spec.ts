import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, RouterModule, RouterLink, provideRouter } from '@angular/router';
import { SidebarComponent } from './sidebar.component';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SidebarComponent,
        RouterModule
      ],
      providers: [
        {
          provide: Router,
          useValue: {
            navigate: jasmine.createSpy('navigate'),
            navigateByUrl: jasmine.createSpy('navigateByUrl'),
            url: '/dashboard',
            events: of({}),
            createUrlTree: jasmine.createSpy('createUrlTree').and.returnValue({}),
            serializeUrl: jasmine.createSpy('serializeUrl').and.returnValue(''),
            parseUrl: jasmine.createSpy('parseUrl').and.returnValue({
              root: {
                children: {},
                segments: []
              },
              queryParams: {},
              fragment: null
            }),
            routerState: {
              snapshot: {
                url: '/dashboard'
              }
            }
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({}),
            queryParams: of({}),
            paramMap: of({
              get: (param: string) => null,
              has: (param: string) => false
            }),
            snapshot: {
              paramMap: {
                get: (param: string) => null,
                has: (param: string) => false
              }
            }
          }
        }
      ],

      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;

  });

  it('should create', () => {

    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
});
